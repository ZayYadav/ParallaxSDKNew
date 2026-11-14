/*
 * TDCore background video: always plays with sound.
 *
 * Browsers only allow audible autoplay after a user gesture or once the site
 * has enough media engagement. The clip therefore starts muted and loops, then
 * unmutes itself at the first tap, key press or click. Once the sound is on it
 * stays on for the rest of the visit; there is no mute control.
 */
(function () {
    'use strict';

    var video = document.querySelector('.bg-video');
    if (!video) return;

    var GESTURES = ['pointerdown', 'touchstart', 'keydown', 'click'];
    var unlock = null;
    var soundIsOn = false;

    function hasAudioTrack() {
        if (video.getAttribute('data-audio') !== '1') return false;
        if (video.currentSrc && video.currentSrc.indexOf('tdcore-brand') !== -1) return false;
        return true;
    }

    function play() {
        try {
            var attempt = video.play();
            if (attempt && typeof attempt.catch === 'function') {
                attempt.catch(function () {});
            }
        } catch (ignored) {}
    }

    function stopUnlock() {
        if (!unlock) return;
        GESTURES.forEach(function (name) {
            document.removeEventListener(name, unlock, true);
        });
        unlock = null;
    }

    /* Policy rejected audible playback: stay muted and wait for a real gesture. */
    function waitForGesture() {
        video.muted = true;
        video.volume = 0;
        play();
        if (unlock) return;
        unlock = function () {
            attemptSound();
        };
        GESTURES.forEach(function (name) {
            document.addEventListener(name, unlock, true);
        });
    }

    function attemptSound() {
        if (soundIsOn) {
            stopUnlock();
            return;
        }
        if (!hasAudioTrack()) {
            stopUnlock();
            return;
        }
        video.muted = false;
        video.volume = 1;
        var attempt;
        try {
            attempt = video.play();
        } catch (ignored) {
            waitForGesture();
            return;
        }
        if (attempt && typeof attempt.then === 'function') {
            attempt.then(function () {
                if (!video.muted && !video.paused) {
                    soundIsOn = true;
                    stopUnlock();
                } else {
                    waitForGesture();
                }
            }).catch(function () {
                waitForGesture();
            });
        } else if (!video.muted) {
            soundIsOn = true;
            stopUnlock();
        }
    }

    /* Chrome exposes whether audible autoplay is allowed for this site. */
    function start() {
        if (!hasAudioTrack()) return;
        /* Try audible playback now; if policy rejects it the clip stays muted
         * and the first tap, key press or click unmutes it for good. */
        attemptSound();
    }

    if (video.readyState >= 1) start();
    else video.addEventListener('loadedmetadata', start, { once: true });

    video.addEventListener('volumechange', function () {
        if (video.muted) soundIsOn = false;
    });
})();
