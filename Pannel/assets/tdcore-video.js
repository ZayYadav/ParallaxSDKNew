/*
 * TDCore background video sound control.
 *
 * Browsers only allow audible autoplay after a user gesture, so the clip starts
 * muted and loops. The first tap/key press (or the sound pill) unmutes it. The
 * choice is remembered for the session so login, register and /connect keep
 * playing with sound as you move through the panel.
 */
(function () {
    'use strict';

    var video = document.querySelector('.bg-video');
    var toggle = document.querySelector('[data-sound-toggle]');
    if (!video) return;

    var STORAGE_KEY = 'tdcore-video-sound';
    var intended = false;
    try { intended = window.sessionStorage.getItem(STORAGE_KEY) === '1'; } catch (ignored) {}

    function remember(on) {
        try { window.sessionStorage.setItem(STORAGE_KEY, on ? '1' : '0'); } catch (ignored) {}
    }

    function hasAudioTrack() {
        if (video.getAttribute('data-audio') !== '1') return false;
        if (video.currentSrc && video.currentSrc.indexOf('tdcore-brand') !== -1) return false;
        try {
            if (video.mozHasAudio) return true;
            if (video.audioTracks && video.audioTracks.length > 0) return true;
            if (typeof video.webkitAudioDecodedByteCount === 'number' &&
                video.webkitAudioDecodedByteCount > 0) return true;
        } catch (ignored) {}
        return true;
    }

    function label(on) {
        if (!toggle) return;
        var text = toggle.querySelector('span');
        if (text) text.textContent = on ? 'Sound off' : 'Sound on';
        toggle.setAttribute('aria-pressed', on ? 'true' : 'false');
        toggle.classList.toggle('is-on', on);
        toggle.title = on ? 'Mute the background clip' : 'Play the background clip with sound';
    }

    function play() {
        try { return video.play(); } catch (ignored) { return null; }
    }

    function apply(on) {
        intended = on;
        video.muted = !on;
        video.volume = on ? 1 : 0;
        var attempt = play();
        label(on);
        return attempt;
    }

    function setSound(on) {
        var attempt = apply(on);
        remember(on);
        return attempt;
    }

    function showToggle() {
        if (!toggle) return;
        toggle.hidden = !hasAudioTrack();
    }

    /* Chrome exposes whether audible autoplay is allowed for this site. */
    function autoplayAllowsSound() {
        try {
            if (typeof navigator.getAutoplayPolicy === 'function') {
                return navigator.getAutoplayPolicy('mediaelement') === 'allowed';
            }
        } catch (ignored) {}
        return false;
    }

    /* A real gesture is the only reliable way to start audible playback. */
    var GESTURES = ['pointerdown', 'touchstart', 'keydown', 'click'];
    var unlock = null;

    function stopAutoUnlock() {
        if (unlock) {
            GESTURES.forEach(function (name) {
                document.removeEventListener(name, unlock, true);
            });
            unlock = null;
        }
    }

    function armAutoUnlock() {
        unlock = function (event) {
            /* Let the sound pill handle its own clicks. */
            if (toggle && event && event.target && toggle.contains(event.target)) return;
            if (!hasAudioTrack()) return;
            var attempt = setSound(true);
            if (attempt && typeof attempt.then === 'function') {
                attempt.then(function () {
                    if (!video.muted) stopAutoUnlock();
                }).catch(function () {
                    /* This gesture was not accepted as audio activation; the
                     * next pointer/click/key attempt stays armed. */
                    video.muted = true;
                    video.volume = 0;
                    label(false);
                });
            } else {
                stopAutoUnlock();
            }
        };
        GESTURES.forEach(function (name) {
            document.addEventListener(name, unlock, true);
        });
    }

    if (toggle) {
        toggle.addEventListener('click', function () {
            stopAutoUnlock();
            if (!hasAudioTrack()) return;
            setSound(!intended);
        });
    }

    video.addEventListener('loadedmetadata', function () {
        showToggle();
        /* Never unmute here: without a gesture Chromium pauses the clip. */
        label(intended && hasAudioTrack());
    });

    /* Keep the pill in sync with native controls or policy-driven pauses. */
    video.addEventListener('volumechange', function () {
        if (video.muted) label(false);
        else if (intended) label(true);
    });

    label(false);
    showToggle();
    if (hasAudioTrack() && autoplayAllowsSound()) {
        /* The site has enough media engagement to autoplay with sound. */
        stopAutoUnlock();
        setSound(true);
    } else {
        armAutoUnlock();
    }
})();
